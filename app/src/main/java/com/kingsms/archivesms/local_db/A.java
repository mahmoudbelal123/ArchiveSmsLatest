package com.kingsms.archivesms.local_db;

import com.kingsms.archivesms.model.NotificationStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class A {
    public static void main(String[] args) {
        List<NotificationStatus> list1 = new ArrayList<>();
        list1.add(new NotificationStatus("A" , 0));
        list1.add(new NotificationStatus("B" , 0));
        list1.add(new NotificationStatus("C" , 0));
        list1.add(new NotificationStatus("A" , 0));
        list1.add(new NotificationStatus("C" , 0));


        Set<NotificationStatus> s = new LinkedHashSet<>(list1);
        list1 = new ArrayList<>(s);
        for (int i = 0 ; i < list1.size() ; i++)
            {
                System.out.println(list1.get(i).getSenderName());
            }
       // Collections.reverse(li);
    }
}
