package com.thoughtworks.fixed.assets.api;

import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UsersResourceNormalTest extends TestBase{
    private String basePath = "/helloworld";





    @Test
    public void should_list_all_users(){
        Response response = target(basePath+"/receipt").request().get();

        assertThat(response.getStatus(), is(200));

        List<Map> result = response.readEntity(List.class);

        assertThat(result.size(), is(2));

        Map user = result.get(0);

    }
    
    @Test
    public void should_create_one_user(){
        Form formData = new Form();

        formData.param("name", "James");
        
        Response response = target(basePath).request().post(Entity.entity(formData, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        assertThat(response.getStatus(), is(201));
        
        Map user = response.readEntity(Map.class);
        
        assertThat((String) user.get("uri"), is(basePath+"/3"));
        assertThat((String) user.get("name"), is("James"));

    }
    
    @Test
    public void should_get_user_by_Id(){
        Response response = target(basePath+"/1").request().get();

        assertThat(response.getStatus(), is(200));
        
        Map user = response.readEntity(Map.class);
        
        assertThat((String) user.get("uri"), is(basePath+"/1"));
        assertThat((String) user.get("name"), is("Name"));
        

    }
    
    

    
}
