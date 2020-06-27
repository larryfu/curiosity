package cn.larry.leetcode;

/**
 * 148. 排序链表
 */
public class ListSort {

    public ListNode sortList(ListNode head) {
        if(head == null || head.next == null) {
            return head;
        }
        ListNode slow = head;
        ListNode fast = head;
        ListNode breakN = head;
        while(fast!=null && fast.next!=null){
            breakN = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        breakN.next = null;
        ListNode left = sortList(head);
        ListNode right =  sortList(slow);
        return  mergeList(left,right);
    }

    private ListNode mergeList(ListNode l,ListNode r){
        ListNode tmpHead = new ListNode(-1);
        ListNode p = tmpHead;
        while(l!=null && r!=null){
            if(l.val < r.val){
                p.next = l;
                l = l.next;
            }else{
                p.next = r;
                r = r.next;
            }
            p = p.next;
            p.next = null;
        }
        if(l!=null) p.next = l;
        if(r!=null) p.next = r;
        return tmpHead.next;
    }
}
