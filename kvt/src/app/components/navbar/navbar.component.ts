import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  standalone:true,
  imports:[RouterOutlet],
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

}
