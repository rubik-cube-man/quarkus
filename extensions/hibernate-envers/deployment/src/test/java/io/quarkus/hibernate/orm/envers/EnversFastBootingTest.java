package io.quarkus.hibernate.orm.envers;

import jakarta.inject.Inject;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.bootstrap.classloading.ClassLoaderLimiter;
import io.quarkus.test.QuarkusUnitTest;
import io.smallrye.common.constraint.Assert;

/**
 * Let's run some checks to verify that the optimisations we have
 * to actually boot efficiently are going to survive other patches.
 */
public class EnversFastBootingTest {

    private static final ClassLoaderLimiter limitsChecker = ClassLoaderLimiter.builder()
            .neverLoadedResource("org/hibernate/jpa/orm_2_1.xsd")
            .neverLoadedResource("org/hibernate/jpa/orm_2_2.xsd")
            .build();

    @RegisterExtension
    static QuarkusUnitTest runner = new QuarkusUnitTest()
            .withApplicationRoot((jar) -> jar
                    .addClass(MyAuditedEntity.class))
            .withConfigurationResource("application.properties")
            .addClassLoaderEventListener(limitsChecker);

    @Inject
    Session session;

    @Test
    public void testInjection() {
        //Check that Hibernate actually started:
        Assert.assertNotNull(session);
    }

}
