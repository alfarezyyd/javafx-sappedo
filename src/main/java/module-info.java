module alfarezyyd.sappedo {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires net.synedra.validatorfx;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;
  requires eu.hansolo.tilesfx;
  requires javafx.graphics;
  requires atlantafx.base;

  opens alfarezyyd.sappedo to javafx.fxml;
  opens alfarezyyd.sappedo.controller to javafx.fxml;
  exports alfarezyyd.sappedo;
  exports alfarezyyd.sappedo.controller;
}