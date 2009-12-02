package se.vgregion.portal.tasklist.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

public class LoggerInjectorTest {

    private LoggerInjector loggerInjector;

    @Before
    public void setUp() throws Exception {
        loggerInjector = new LoggerInjector();
    }

    @Test
    public void testPostProcessAfterInitialization() {
        assertEquals(TestBean.class, loggerInjector.postProcessAfterInitialization(new TestBean(), null)
                .getClass());
    }

    @Test
    public void testPostProcessBeforeInitialization() {
        TestBean testBean = new TestBean();
        TestBean testBeanResult = (TestBean) loggerInjector.postProcessBeforeInitialization(testBean, null);
        assertNotNull(testBeanResult.logger);
    }

    class TestBean {
        @se.vgregion.portal.tasklist.services.Logger
        private Logger logger;
    }

}
