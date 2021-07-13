import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Customer } from '../../Customer';
import { UiService } from '../../services/ui.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-add-customer',
  templateUrl: './add-customer.component.html',
  styleUrls: ['./add-customer.component.css']
})
export class AddCustomerComponent implements OnInit {
  @Output() onAddCustomer: EventEmitter<Customer> = new EventEmitter();
  name!: string;
  showAddCustomer!: boolean;
  subscription!: Subscription;

  constructor(private uiService:UiService) {
    this.subscription = this.uiService.onToggle().subscribe(
      (value => this.showAddCustomer = value)
    );
  }

  ngOnInit(): void {
  }

  onSubmit() {
    if (!this.name) {
      alert('Please provide customer name!');
      return;
    }

    const newCustomer = {
      name: this.name
    }
    this.onAddCustomer.emit(newCustomer);

    this.name = '';
  }
}
