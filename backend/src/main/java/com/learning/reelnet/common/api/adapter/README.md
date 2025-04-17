# API Adapters

This directory contains adapter classes that help provide backward compatibility and facilitate the transition between different API response formats.

## Components

- `ResponseAdapter` - Provides methods to convert between legacy DTO response formats and the new standardized API response objects. This adapter is marked as deprecated and will be removed in a future release.

## Purpose

The adapters in this directory serve as a temporary bridge to help migrate code from older, less structured response formats to the new standardized API response objects. They provide a way to gradually update code without breaking existing functionality.

## Usage Example

```java
// Instead of directly using legacy DTO response classes:
// import com.learning.reelnet.common.application.dto.ApiResponse;
// ApiResponse<User> response = ApiResponse.success(user);

// Use the adapter during transition:
import com.learning.reelnet.common.api.adapter.ResponseAdapter;
import com.learning.reelnet.common.api.response.ApiResponse;

ApiResponse<User> response = ResponseAdapter.success(user);
```

## Deprecation Notice

The classes in this directory are marked with `@Deprecated(forRemoval = true)` to indicate that they will be removed in a future version. Applications should migrate to directly use the classes in the `api/response` package as soon as possible. 