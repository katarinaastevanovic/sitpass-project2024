

export interface Facility {
    id?: number;
    name?: string;
    description: string;
    createdAt?: Date;
    address: string;
    city: string;
    totalRating?: number;
    active?: boolean;
    images?: any[];
    isDeleted?: boolean | null;
}