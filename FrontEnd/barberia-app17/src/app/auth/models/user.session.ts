import { Authority } from './authority.interface';

export interface UserSession {
  username: string;
  email: string;
  token: string;
  authorities: Authority[];
}
