export interface User {
    id?: number;
    email?: string;
    name?: string;
    surname?: string;
    phoneNumber?: string;
    address?: string;
    birthday?: string; // Formatiraj kao string jer će biti u ISO formatu prilikom preuzimanja iz backend-a
    createdAt?: string;
    zipCode?: string;
    city?: string;
}
