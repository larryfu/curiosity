package cn.larry.demo;

import java.util.Arrays;

public class AddTwoNum {

      public class ListNode {
          int val;
          ListNode next;
          ListNode(int x) { val = x; }
          int[] ints = new int[]{1,2};
      }

        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

            int rt = l1.val+l2.val;
            ListNode r = new ListNode(rt%10);
            int jinwei = rt/10;
            l1=l1.next;l2=l2.next;
            ListNode current = r;
            if(jinwei>0)
                current.next = new ListNode(jinwei);
            while(l1!=null||l2!=null){
                if(current.next==null){
                    current.next = new ListNode(0);
                }
                current = current.next;
                int result = (l1!=null?l1.val:0) + (l2!=null?l2.val:0) + current.val;
                jinwei = result/10;
                current.val = result%10;
                if(jinwei>0){
                    current.next = new ListNode(jinwei);
                }
                if(l1!=null) l1= l1.next;
                if(l2!=null) l2 = l2.next;
            }
            return r;
        }

}
