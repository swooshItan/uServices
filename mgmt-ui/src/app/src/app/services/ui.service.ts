import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UiService {
  private showAddCustomer: boolean = false;
  private subject = new Subject<any>();

  toggleAddCustomer(): void {
    this.showAddCustomer = !this.showAddCustomer;
    this.subject.next(this.showAddCustomer);
  }

  onToggle(): Observable<any> {
    return this.subject.asObservable();
  }
}
