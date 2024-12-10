export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  contactNumber: string;
  userRole: 'ROLE_CUSTOMER' | 'ROLE_VENDOR' | 'ROLE_ADMIN';
  createdAt: Date;
}

export interface UserUpdateRequest {
  firstName: string;
  lastName: string;
  contactNumber: string;
}
