package com.springmsproject.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "t_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
   // JPA repository we need to use Entity and Need to define unique id
   // and use GeneratedValue(strategy ) to define the type of generation
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private String orderNumber;
   //Need to define the relationship of the attributes
   @OneToMany(cascade = CascadeType.ALL) //Cascading type which helps to make sync between the tables
   private List<OrderLineItems> orderLineItemList;
}
