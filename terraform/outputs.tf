output "vpc_id" {
  description = "ID of the VPC"
  value       = module.vpc.vpc_id
}

output "public_subnet_ids" {
  description = "IDs of the public subnets"
  value       = module.vpc.public_subnet_ids
}

output "private_subnet_ids" {
  description = "IDs of the private subnets"
  value       = module.vpc.private_subnet_ids
}

output "rds_endpoint" {
  description = "Endpoint of the RDS instance"
  value       = module.rds.db_endpoint
}

output "redis_endpoint" {
  description = "Endpoint of the ElastiCache cluster"
  value       = module.elasticache.redis_endpoint
}

output "s3_bucket_name" {
  description = "Name of the S3 bucket"
  value       = module.s3.bucket_name
}

output "cloudfront_domain_name" {
  description = "Domain name of the CloudFront distribution"
  value       = module.cloudfront.domain_name
}

output "alb_dns_name" {
  description = "DNS name of the Application Load Balancer"
  value       = module.alb.dns_name
}

output "frontend_service_name" {
  description = "Name of the frontend ECS service"
  value       = module.frontend_service.service_name
}

output "backend_service_name" {
  description = "Name of the backend ECS service"
  value       = module.backend_service.service_name
}

output "ai_service_name" {
  description = "Name of the AI service"
  value       = module.ai_service.service_name
} 