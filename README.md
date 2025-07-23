Simple Employee CRUD App ( University Demo Project ) 
🚀 Overview

A Simple Employee CRUD application using Spring Boot + AWS Lambda Serverless + AWS API Gateway + AWS RDS (MySQL) + AWS ElastiCache (Redis/Memcached) + S3 Static Hosting.

This project demonstrates modern cloud-native backend architecture with clean CRUD APIs on a large dataset (~300K employee records) and efficient query caching.

🛠️ Technologies Used

  Java + Spring Boot (Serverless-ready backend) 
  
  AWS Lambda (Serverless compute)
  
  AWS API Gateway (API routing)
  
  AWS RDS (MySQL) with the Sample Employee Database https://github.com/datacharmer/test_db
  
  AWS ElastiCache (Redis / Memcached) for caching
  
  AWS S3 for static web hosting
  
  HTML, CSS, JavaScript (simple frontend)

✨ Features

✅ CRUD Operations on the employees table (Create, Read, Update, Delete)
✅ Top 10 high-salary employees display with optimized caching
✅ Serverless architecture for scalability and cost efficiency
✅ Caching integration to improve heavy query performance
✅ Static frontend hosted on AWS S3
✅ API-first architecture for integration with any frontend or microservice

📊 Key Query

Displays top 10 current employees with salaries higher than the average using a heavy SQL join, demonstrating cache performance benefits.

🌐 Demo Access

  Landing Page: Displays top 10 high-salary employees in less than 3 seconds

  API Endpoints: Serverless CRUD via AWS Lambda + API Gateway

🏗️ Architecture Highlights

  Clean separation between frontend (S3) and backend (Lambda + RDS)

  Elasticache reduces latency for heavy, repeated queries

  Utilizes large real-world dataset (~300K records) for practical performance demonstration

  Fully serverless for low maintenance and operational simplicity
