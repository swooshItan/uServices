import { Component, OnInit, Input } from '@angular/core';
import { UiService } from '../../services/ui.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  title = 'Customer-Mgmt';
  showAddCustomer!: boolean;
  subscription!: Subscription;

  constructor(private uiService:UiService) {
    this.subscription = this.uiService.onToggle().subscribe(
      (value => this.showAddCustomer = value)
    );
  }

  ngOnInit(): void {
  }

  toggleAddCustomer() {
    this.uiService.toggleAddCustomer();
  }
}
