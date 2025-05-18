import { Discipline } from "./discipline";
import { WorkDay } from "./work-day";

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
    workDays?: WorkDay[];
    disciplines?: Discipline[];
    isDeleted?: boolean | null;
}