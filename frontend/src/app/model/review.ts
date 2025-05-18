export interface Review {
    id: number;
    rate: {
      total: number;
      equipment: number;
      staff: number;
      hygene: number;
      space: number;
    };
    userId: number;
    facilityId: number;
    comments: CommentDto[]; // Polje comments je niz objekata CommentDto
    createdAt: string;
  }
  
  export interface CommentDto {
    id: number;
    text: string;
    createdAt: string;
    userEmail: string;
  }
  