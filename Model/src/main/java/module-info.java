module pl.first.firstjava {
    requires org.apache.commons.lang3;
    requires javafx.controls;
    requires log4j;
    requires java.sql;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;

    exports pl.first.firstjava;
    opens pl.first.firstjava;
    exports pl.first.firstjava.exceptions;
}