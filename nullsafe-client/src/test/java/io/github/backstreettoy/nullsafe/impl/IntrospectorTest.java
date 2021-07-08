package io.github.backstreettoy.nullsafe.impl;

import org.junit.Test;

import java.beans.*;

public class IntrospectorTest {

    @Test
    public void testIntrospector() throws IntrospectionException {

        BeanInfo beanInfo = Introspector.getBeanInfo(TestBean.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : propertyDescriptors) {
            if ("class".equals(pd.getName())) {
                continue;
            }

            System.out.println(pd.getReadMethod().getName());
            System.out.println(pd.getWriteMethod().getName());
        }

    }

    public static class TestBean {
        private Long longValue = 99999923234323L;
        private int intValue = 1;

        private boolean booleanValue = true;

        public Long getLongValue() {
            return longValue;
        }

        public void setLongValue(Long longValue) {
            this.longValue = longValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        @Transient(true)
        public boolean isBooleanValue() {
            return booleanValue;
        }

        public void setBooleanValue(boolean booleanValue) {
            this.booleanValue = booleanValue;
        }

        public String getStr() {
            return null;
        }

        public void setStr(String s) {

        }
    }
}
