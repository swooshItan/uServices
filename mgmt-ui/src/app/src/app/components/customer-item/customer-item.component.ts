import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Customer } from '../../Customer';

@Component({
  selector: 'app-customer-item',
  templateUrl: './customer-item.component.html',
  styleUrls: ['./customer-item.component.css']
})
export class CustomerItemComponent implements OnInit {
  @Input() customer!: Customer;
  @Output() onDeleteCustomer: EventEmitter<Customer> = new EventEmitter();
  select: boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

  deleteCustomer(customer:Customer) {
    this.onDeleteCustomer.emit(customer);
  }

  selectCustomer() {
    this.select = !this.select;
  }
}
