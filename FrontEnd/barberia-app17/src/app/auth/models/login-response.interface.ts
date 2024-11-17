import { Authority } from './authority.interface';

export interface LoginResponse {
  email: string;
  userId: number;
  authorities: Authority[];
  username: string;
  token: string;
}
