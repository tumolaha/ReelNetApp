terraform {
  required_version = ">= 1.0.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# VPC Module
module "vpc" {
  source = "./modules/vpc"

  project_name         = var.project_name
  environment          = var.environment
  vpc_cidr            = var.vpc_cidr
  availability_zones  = var.availability_zones
}

# RDS Module
module "rds" {
  source = "./modules/rds"

  project_name         = var.project_name
  environment          = var.environment
  vpc_id              = module.vpc.vpc_id
  private_subnet_ids  = module.vpc.private_subnet_ids
  db_username         = var.db_username
  db_password         = var.db_password
  db_instance_class   = var.db_instance_class
  allocated_storage   = var.db_allocated_storage
}

# ElastiCache Module
module "elasticache" {
  source = "./modules/elasticache"

  project_name         = var.project_name
  environment          = var.environment
  vpc_id              = module.vpc.vpc_id
  private_subnet_ids  = module.vpc.private_subnet_ids
  node_type           = var.redis_node_type
  num_cache_nodes     = var.redis_num_cache_nodes
}

# S3 Module
module "s3" {
  source = "./modules/s3"

  project_name         = var.project_name
  environment          = var.environment
}

# CloudFront Module
module "cloudfront" {
  source = "./modules/cloudfront"

  project_name         = var.project_name
  environment          = var.environment
  s3_bucket_name      = module.s3.bucket_name
}

# ALB Module
module "alb" {
  source = "./modules/alb"

  project_name         = var.project_name
  environment          = var.environment
  vpc_id              = module.vpc.vpc_id
  public_subnet_ids   = module.vpc.public_subnet_ids
  private_subnet_ids  = module.vpc.private_subnet_ids
}

# Frontend Service Module
module "frontend_service" {
  source = "./modules/frontend_service"

  project_name         = var.project_name
  environment          = var.environment
  vpc_id              = module.vpc.vpc_id
  private_subnet_ids  = module.vpc.private_subnet_ids
  alb_target_group_arn = module.alb.frontend_target_group_arn
  desired_count       = var.frontend_desired_count
}

# Backend Service Module
module "backend_service" {
  source = "./modules/backend_service"

  project_name         = var.project_name
  environment          = var.environment
  vpc_id              = module.vpc.vpc_id
  private_subnet_ids  = module.vpc.private_subnet_ids
  alb_target_group_arn = module.alb.backend_target_group_arn
  desired_count       = var.backend_desired_count
  db_endpoint         = module.rds.db_endpoint
  redis_endpoint      = module.elasticache.redis_endpoint
}

# AI Service Module
module "ai_service" {
  source = "./modules/ai_service"

  project_name         = var.project_name
  environment          = var.environment
  vpc_id              = module.vpc.vpc_id
  private_subnet_ids  = module.vpc.private_subnet_ids
  alb_target_group_arn = module.alb.ai_target_group_arn
  desired_count       = var.ai_service_desired_count
}

# Variables
variable "dockerhub_username" {
  description = "Docker Hub username"
  type        = string
}

variable "db_username" {
  description = "Database username"
  type        = string
}

variable "db_password" {
  description = "Database password"
  type        = string
  sensitive   = true
}

variable "db_name" {
  description = "Database name"
  type        = string
  default     = "reelnet"
}

variable "mongodb_username" {
  description = "MongoDB username"
  type        = string
}

variable "mongodb_password" {
  description = "MongoDB password"
  type        = string
  sensitive   = true
}

# Data sources
data "aws_availability_zones" "available" {
  state = "available"
} 