import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Customer } from '../Customer';
import { CUSTOMERS } from '../mock-customers';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
}

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = 'http://192.168.99.103:30777/customers';

  constructor(private httpClient:HttpClient) { }

   getCustomers(): Observable<Customer[]> {
     return this.httpClient.get<Customer[]>(
       this.apiUrl);

    //const customers = of(CUSTOMERS);
    //return customers;
  }

  deleteCustomer(customer:Customer): Observable<Customer> {
    return this.httpClient.delete<Customer>(
      `${this.apiUrl}/${customer.id}`);
  }

  addCustomer(customer:Customer): Observable<Customer> {
    return this.httpClient.post<Customer>(
      this.apiUrl, customer, httpOptions);
  }
}
