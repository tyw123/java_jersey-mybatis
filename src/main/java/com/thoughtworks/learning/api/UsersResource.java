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
    @Path("/receipt")
    @GET
    @Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
    public Response getreceipt(){
        List<Itemp> itemInput = ItempRepository.findItemp();
        HashMap result=new HashMap();
        float total=0;
        float saved=0;
      //  List<Map> item_final=new ArrayList();
        ArrayList  itemFinal=new ArrayList();
        ArrayList  giftFinal=new ArrayList();
        List<ArrayList> items=new ArrayList();
        for(int i=0;i<itemInput.size();i++){//输入itemp字符处理--取出barcode和num
            ArrayList  item=new ArrayList();
            String [] ars =itemInput.get(i).getInfo().split("-");
            int num = Integer.parseInt(ars[1]);
            item.add(ars[0]);
            item.add(num);
            if(items.size()==0){ //合并同类项,获得输入的barcode和数量
                items.add(item);
            }else{
                int flag=0;
                for(int j=0;j<items.size();j++){
                    // System.out.print(items.get(j).get(1).getClass().getName()+"\n");
                    if(ars[0].equals(items.get(j).get(0))){
                        int add= (int) items.get(j).get(1);
                        items.get(j).set(1,add+num);
                        flag=1;
                    }
                }
                if(flag==0){items.add(item);}
            }
        }

        //取出对应的商品信息
        for(int i=0;i<items.size();i++){
            HashMap item=new HashMap();
            HashMap gift=new HashMap();
            //查找
            String itemBarcode= (String) items.get(i).get(0);
            Allitem itemSearch = AllitemRepository.getItemInfo(itemBarcode);


            item.put("barcode",itemBarcode);
            item.put("name",itemSearch.getName());
            item.put("price",itemSearch.getPrice());
            item.put("unit",itemSearch.getUnit());
            item.put("num",items.get(i).get(1));
            int num= (int) items.get(i).get(1);
            float money=itemSearch.getPrice()*num;
            item.put("total",money);
           // System.out.print(item+"\n");
            total+=money;
            gift.put("barcode",itemBarcode);
            gift.put("name",itemSearch.getName());
            gift.put("price",itemSearch.getPrice());
            gift.put("unit",itemSearch.getUnit());
            Forsave promoSearch=ForsaveRepository.getPromoInfo(itemBarcode);
            if(promoSearch!=null){
                int promoNum=num/3;
                gift.put("num",promoNum);
                saved+=itemSearch.getPrice()*promoNum;
            }else{
                gift.put("num",0);
            }
            itemFinal.add(item);
            giftFinal.add(gift);
         //   System.out.print(gift);
        }
        result.put("total", total);
        result.put("saved", saved);
        result.put("item", itemFinal);
        result.put("gift", giftFinal);
      //  System.out.print(items.size());
        return Response.status(200).entity(result).build();
    }

}
