/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magenta.maxoptra;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

/**
 *
 * @author gavrilov
 */
@ApplicationPath("/rest") //говорим, что корень приложения , можно в корне просто пустым "/"
public class SalesApp extends Application{   
}