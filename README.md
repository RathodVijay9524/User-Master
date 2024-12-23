http://localhost:9091/api/auth/login
{
  "username": "rathod",
  "password": "rathod"
}
post for Register user -> http://localhost:9091/api/auth/users




{
    "responseStatus": "OK",
    "status": "success",
    "message": "success",
    "data": {
        "jwtToken": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJyYXRob2QiLCJpYXQiOjE3MzQ5NDU3MjksImV4cCI6MTczNTU1MDUyOX0.i71Rv_3DKo344WU8GoAX9ld54JTwS-7aSnA5m4s0et9ymHpWCZPus6Wy2jqhTVtL",
        "user": {
            "id": 4,
            "name": "Vijay Rathod",
            "username": "rathod",
            "email": "rathod@gmail.com",
            "password": "$2a$10$/pSgEYtwgA1BYYus0TtPZOwfVmpaJGbSPWi5Mz1mbTk48UVAuGE0m",
            "roles": [
                {
                    "name": "ROLE_USER",
                    "createdBy": 1,
                    "updatedBy": null,
                    "createdOn": "2024-12-22T12:51:29.676+00:00",
                    "updatedOn": null,
                    "id": 2,
                    "active": true,
                    "deleted": false
                }
            ],
            "active": true,
            "deleted": false
        }
    }
}
