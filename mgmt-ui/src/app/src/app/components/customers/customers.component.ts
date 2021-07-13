import { Component, OnInit } from '@angular/core';
import {CustomerService} from '../../services/customer.service';
import {Customer} from '../../Customer';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit {
  customers: Customer[] = [];

  constructor(private customerService: CustomerService) { }

  ngOnInit(): void {
    this.customerService.getCustomers()
      .subscribe((customers) => {
        this.customers = customers;
      });
  }

  deleteCustomer(customer:Customer) {
    this.customerService.deleteCustomer(customer)
      .subscribe(() => (
        this.customers = this.customers.filter((c) => 
          c.id !== customer.id
        )
      ));
  }

  addCustomer(customer:Customer) {
    this.customerService.addCustomer(customer)
      .subscribe((customer) => (
        this.customers.push(customer)
      ));
  }
}
