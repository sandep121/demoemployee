package com.sandeep.demoemployee.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandeep.demoemployee.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Locale;
@Service
public class Messages implements MessageSourceAware {

    private MessageSource messageSource;
    @Override
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource=messageSource;
    }
    public String getMessage(String message)
     {
         return this.messageSource.getMessage(message,null, Locale.US);
     }
    public String getMessage(Employee emp)
     {
         ObjectMapper obj=new ObjectMapper();
         String jsonStr=null;
         try
         {
             jsonStr = obj.writeValueAsString(emp);
         }
         catch (IOException e) {
             e.printStackTrace();
         }
         return this.messageSource.getMessage(jsonStr,null, Locale.US);
     }

}
