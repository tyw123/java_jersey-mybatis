package com.thoughtworks.learning.api;

import com.thoughtworks.learning.core.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/helloworld")
public class UsersResource {

    @Inject
    private AllitemRepository AllitemRepository;

    @Inject
    private ForsaveRepository ForsaveRepository;

    @Inject
    private ItempRepository ItempRepository;

    @Path("/promotion")
    @GET
    @Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
    public Response promotion(){
        HashMap save=new HashMap();
        ArrayList promo=new ArrayList();
        ArrayList forsave=new ArrayList();
        List<Forsave> list = ForsaveRepository.findForsave();
        for(Forsave Forsave : list){  //这么用for有点炫酷

          //  userBean.put("type",Forsave.getType());
            promo.add(Forsave.getBarcode());
         //   temp.add(userBean);
        }
        save.put("type","BUY_TWO_GET_ONE_FREE");
        save.put("barcode",promo);
        forsave.add(save);
        return Response.status(200).entity(forsave).build();
    }

    @Path("/all_item")
    @GET
    @Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
    public Response allitem(){
        List<Map> result = new ArrayList<>();
        List<Allitem> list = AllitemRepository.findAllitem();
        for(Allitem Allitem : list){  //这么用for有点炫酷
            Map userBean = new HashMap();
            userBean.put("unit",Allitem.getUnit());
            userBean.put("price",Allitem.getPrice());
            userBean.put("name",Allitem.getName());
            userBean.put("barcode",Allitem.getBarcode());
            result.add(userBean);
        }
        
        return Response.status(200).entity(result).build();
    }

    @Path("/input")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response insert_inputs(@FormParam("itemp") String itemp){
        System.out.println(String.format(" %s",itemp));
      //  ArrayList arrayinput=JSON
//        for(int i=0;i<itemp.length();i++){
//            String newInstanceBean=itemp[i];
//
//
//        };
      //  Map result = new HashMap();
        Map newInstanceBean = new HashMap();
        newInstanceBean.put("info", itemp);

        ItempRepository.createItemp(newInstanceBean);

      //  result.put("info", newInstanceBean.get("itemp"));
       // result.put("name",newInstanceBean.get("name"));



        return Response.status(201).entity(newInstanceBean).build();
    }

    @Path("/input")
    @GET
    @Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
    public Response getinput(){
       // List<Map> result = new ArrayList<>();
        List<Itemp> list = ItempRepository.findItemp();
        ArrayList userBean = new ArrayList();
        for(Itemp Itemp : list){  //这么用for有点炫酷

            userBean.add(Itemp.getInfo());
         //   result.add(userBean);
        }

        return Response.status(200).entity(userBean).build();
    }
//    @Path("/")
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response create(@FormParam("name") String name){
//        Map result = new HashMap();
//        Map newInstanceBean = new HashMap();
//        newInstanceBean.put("name", name);
//
//         userRepository.createUser(newInstanceBean);
//
//        result.put("uri", "/users/"+newInstanceBean.get("id"));
//        result.put("name",newInstanceBean.get("name"));
//
//
//
//        return Response.status(201).entity(result).build();
//    }
    
//    @Path("/{userId}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getById(@PathParam("userId") int userId, @Context SecurityContext context){
//        System.out.println(userId);
//        Map result = new HashMap();
//        User instance = userRepository.getUserById(userId);
//        result.put("uri", "/users/"+userId);
//        result.put("name", instance.getName());
//        return Response.status(200).entity(result).build();//打印失败，不明原因
//    }
}
