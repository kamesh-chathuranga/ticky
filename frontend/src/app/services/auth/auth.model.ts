export interface LogInResponse {
  token: string;
  message: string;
}

export interface LogInRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  contactNumber: string;
  role: string;
}
