package cn.larry.leetcode;

/**
 * 141. 环形链表
 *
 */
public class CycleList {


    public  ListNode detectCycle(ListNode head){
        ListNode cycleNode = hasCycle(head);
        if(cycleNode == null ){
            return null;
        }
        ListNode node = head;
        while(cycleNode!=node){
            cycleNode = cycleNode.next;
            node = node.next;
        }
        return node;
    }


    public ListNode hasCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while(fast != null){
            fast = fast.next;
            if(fast == null){
                return null;
            }
            fast = fast.next;
            slow = slow.next;
            if(slow == fast){
                return slow;
            }
        }
        return null;
    }
}
