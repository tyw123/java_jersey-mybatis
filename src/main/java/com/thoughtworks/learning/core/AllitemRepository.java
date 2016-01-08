package com.thoughtworks.learning.core;

import java.util.List;
import java.util.Map;

public interface AllitemRepository {

    List<Allitem> findAllitem();
//    List<Forsave> findForsave();
//    List<Itemp> findItemp();
//
//    void createItemp(Map newInstance);

    Allitem getItemInfo(String barcode);
    
}
