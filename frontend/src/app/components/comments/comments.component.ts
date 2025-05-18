import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-facility-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css'],
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule] 
})
export class FacilityCommentsComponent implements OnInit {
  facilityId!: number;
  comments: any[] = [];
  replyToCommentId: number | null = null; 
  replyText: string = '';
  loggedInUserEmail: string = ''; 

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit(): void {
    // Dohvatanje emaila ulogovanog korisnika iz localStorage koristeći ključ 'email'
    this.loggedInUserEmail = localStorage.getItem('email') || '';

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.facilityId = +id;
        this.getCommentsByFacilityId();
      }
    });
}


  getCommentsByFacilityId() {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
    });
    
    this.http.get<any[]>(`http://localhost:8080/api/comments/facility/${this.facilityId}`, { headers }).subscribe(
      response => {
        // Filtriramo komentare tako da glavni komentari nemaju parentId
        this.comments = response.filter(comment => !comment.parentId);
        // Svakom komentaru dodajemo njegove odgovore (replies)
        this.comments.forEach(comment => {
          comment.replies = response.filter(reply => reply.parentId === comment.id);
        });
        console.log(this.comments);
      },
      error => {
        console.error('Error fetching comments', error);
      }
    );
  }

  showReplyForm(commentId: number) {
    this.replyToCommentId = commentId;
  }

  postReply(parentCommentId: number) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
    });

    const replyData = {
      text: this.replyText,
      parentId: parentCommentId,
      reviewId: this.comments.find(comment => comment.id === parentCommentId)?.reviewId,
    };

    this.http.post(`http://localhost:8080/api/comments`, replyData, { headers }).subscribe(
      () => {
        this.getCommentsByFacilityId();
        this.replyToCommentId = null;
        this.replyText = '';
      },
      error => {
        console.error('Error posting reply', error);
      }
    );
  }
}
