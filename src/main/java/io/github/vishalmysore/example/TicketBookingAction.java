package io.github.vishalmysore.example;

import com.t4a.annotations.Action;
import com.t4a.annotations.Agent;
import com.t4a.detect.ActionCallback;
import com.t4a.detect.ActionState;

import java.util.Date;

@Agent(groupName = "ticket booking", groupDescription = "actions related to ticket booking")
public class TicketBookingAction {
    private ActionCallback actionCallback;
     @Action(description = "book a ticket")
     public String bookTicket(String name, String source, String destination, String date) {
         actionCallback.sendtStatus("booking your ticket from "+source+" to "+destination+" for "+date.toString(), ActionState.WORKING);
         //do all the ticket booking logic here
         // call you api etc
         actionCallback.sendtStatus("Your Ticket has been booked", ActionState.COMPLETED);
         return "Ticket booked for " + name + " from " + source + " to " + destination;
     }

     @Action(description = "cancel a ticket")
     public String cancelTicket(String name, String ticketId) {
         if(actionCallback!= null) {
             actionCallback.sendtStatus("cancelling your ticket", ActionState.WORKING);
         }
         return "Ticket with ID " + ticketId + " cancelled for " + name;
     }
}
