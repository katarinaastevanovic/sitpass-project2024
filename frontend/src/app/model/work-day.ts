import { DayOfWeek } from "./enum/day-of-week";
import { LocalTime } from "./local-time";

export interface WorkDay {
    id?: number;
    facilityId?: number;
    validFrom: Date;
    day: DayOfWeek;
    fromTime?: LocalTime;
    untilTime?: LocalTime;
}