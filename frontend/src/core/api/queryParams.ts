import { SortDirection } from "../constants/SortDirection";

/**
 * Search criteria interface for advanced filtering
 */
export interface SearchCriteria {
  field: string;
  operation: string;
  value: any;
  orPredicate?: boolean;
}

/**
 * Base interface for all query parameters
 * Corresponds to the Java BaseFilterRequest class
 */
export interface BaseQueryParams {
  // Basic pagination
  page?: number;
  size?: number;

  // Basic sorting
  sortBy?: string;
  sortDirection?: SortDirection | "asc" | "desc";

  // Multiple sort criteria
  sortCriteria?: string[];

  // Search functionality
  searchText?: string;
  searchFields?: string[];
  searchCriteriaList?: SearchCriteria[];

  // Date filters
  createdAfter?: string; // ISO date string
  createdBefore?: string;
  updatedAfter?: string;
  updatedBefore?: string;

  // User filters
  createdBy?: string;

  // Projection and grouping
  fields?: Set<string> | string[];
  groupBy?: string[];

  // Cache and deleted items
  useCache?: boolean;
  includeDeleted?: boolean;

  // Additional options
  options?: Record<string, any>;

  // Allow additional properties
  [key: string]: any;
}

/**
 * Normalizes query parameters to ensure valid values
 */
export function normalizeQueryParams(params: BaseQueryParams): BaseQueryParams {
  const result = { ...params };

  // Normalize pagination with validation
  if (typeof result.page !== 'number' || result.page < 0) {
    result.page = 0;
  }

  if (typeof result.size !== 'number' || result.size < 1 || result.size > 100) {
    result.size = 10; // Default page size with upper limit
  }

  // Clean up empty search text and trim whitespace
  if (result.searchText) {
    result.searchText = result.searchText.trim();
    if (result.searchText === "") {
      result.searchText = undefined;
    }
  }

  // Ensure sortDirection is valid
  if (result.sortDirection) {
    const direction = String(result.sortDirection).toLowerCase();
    result.sortDirection = (direction === "asc" || direction === "desc") ? direction : "desc";
  }

  // Ensure fields is an array if it's a Set
  if (result.fields instanceof Set) {
    result.fields = Array.from(result.fields);
  }

  // Ensure boolean values have defaults with explicit type checking
  result.useCache = result.useCache === true;
  result.includeDeleted = result.includeDeleted === true;

  // Validate and normalize date filters
  const dateFields = ['createdAfter', 'createdBefore', 'updatedAfter', 'updatedBefore'];
  dateFields.forEach(field => {
    if (result[field]) {
      try {
        // Validate ISO date string
        new Date(result[field]).toISOString();
      } catch (e) {
        // If invalid date, remove the field
        result[field] = undefined;
      }
    }
  });

  return result;
}

/**
 * Converts a complex object to a flat object with dot notation keys
 * @param obj The object to flatten
 * @param prefix The prefix for nested keys
 * @returns A flattened object
 */
function flattenObject(obj: any, prefix = ''): Record<string, any> {
  const result: Record<string, any> = {};
  
  for (const key in obj) {
    if (obj.hasOwnProperty(key)) {
      const value = obj[key];
      const newKey = prefix ? `${prefix}.${key}` : key;
      
      if (value && typeof value === 'object' && !Array.isArray(value) && !(value instanceof Date)) {
        // Recursively flatten nested objects
        Object.assign(result, flattenObject(value, newKey));
      } else {
        result[newKey] = value;
      }
    }
  }
  
  return result;
}

/**
 * Creates URLSearchParams from query parameters for REST API calls
 * Handles all special cases automatically
 */
export function createUrlSearchParams(params: BaseQueryParams): URLSearchParams {
  const searchParams = new URLSearchParams();
  const normalized = normalizeQueryParams(params);
  
  // Process each parameter
  Object.entries(normalized).forEach(([key, value]) => {
    if (value === undefined || value === null) {
      return;
    }
    
    // Handle arrays - using Spring Boot compatible format
    if (Array.isArray(value)) {
      // Special case for searchCriteriaList
      if (key === 'searchCriteriaList') {
        value.forEach((criteria, index) => {
          searchParams.append(
            `searchCriteriaList[${index}].field`,
            encodeURIComponent(criteria.field)
          );
          searchParams.append(
            `searchCriteriaList[${index}].operation`,
            encodeURIComponent(criteria.operation)
          );
          searchParams.append(
            `searchCriteriaList[${index}].value`,
            encodeURIComponent(String(criteria.value))
          );
          if (criteria.orPredicate !== undefined) {
            searchParams.append(
              `searchCriteriaList[${index}].orPredicate`,
              String(criteria.orPredicate)
            );
          }
        });
      } else {
        // Handle regular arrays
        value.forEach((item) => {
          if (item !== undefined && item !== null) {
            searchParams.append(key, encodeURIComponent(String(item)));
          }
        });
      }
      return;
    }
    
    // Handle objects (like options)
    if (typeof value === 'object' && value !== null) {
      if (key === 'options') {
        // Handle options object
        Object.entries(value).forEach(([optKey, optValue]) => {
          if (optValue !== undefined && optValue !== null) {
            searchParams.append(optKey, encodeURIComponent(String(optValue)));
          }
        });
      } else if (value instanceof Date) {
        // Handle Date objects
        searchParams.append(key, encodeURIComponent(value.toISOString()));
      } else if (value instanceof Set) {
        // Handle Set objects
        Array.from(value).forEach((item) => {
          if (item !== undefined && item !== null) {
            searchParams.append(key, encodeURIComponent(String(item)));
          }
        });
      } else {
        // Handle other objects by flattening them
        const flattened = flattenObject(value, key);
        Object.entries(flattened).forEach(([flatKey, flatValue]) => {
          if (flatValue !== undefined && flatValue !== null) {
            searchParams.append(flatKey, encodeURIComponent(String(flatValue)));
          }
        });
      }
      return;
    }
    
    // Handle simple values with encoding
    searchParams.append(key, encodeURIComponent(String(value)));
  });
  
  return searchParams;
}

/**
 * Converts query parameters to a format suitable for Axios params
 * This function handles all special cases and returns a flat object
 * that can be directly used with Axios
 */
export function createAxiosParams(params: BaseQueryParams): Record<string, any> {
  const normalized = normalizeQueryParams(params);
  const result: Record<string, any> = {};
  
  // Process each parameter
  Object.entries(normalized).forEach(([key, value]) => {
    if (value === undefined || value === null) {
      return;
    }
    
    // Handle arrays
    if (Array.isArray(value)) {
      // Special case for searchCriteriaList
      if (key === 'searchCriteriaList') {
        value.forEach((criteria, index) => {
          result[`searchCriteriaList[${index}].field`] = criteria.field;
          result[`searchCriteriaList[${index}].operation`] = criteria.operation;
          result[`searchCriteriaList[${index}].value`] = criteria.value;
          if (criteria.orPredicate !== undefined) {
            result[`searchCriteriaList[${index}].orPredicate`] = criteria.orPredicate;
          }
        });
      } else {
        // For regular arrays, we'll let Axios handle them
        result[key] = value;
      }
      return;
    }
    
    // Handle objects
    if (typeof value === 'object' && value !== null) {
      if (key === 'options') {
        // Handle options object
        Object.entries(value).forEach(([optKey, optValue]) => {
          if (optValue !== undefined && optValue !== null) {
            result[optKey] = optValue;
          }
        });
      } else if (value instanceof Date) {
        // Handle Date objects
        result[key] = value.toISOString();
      } else if (value instanceof Set) {
        // Handle Set objects
        result[key] = Array.from(value);
      } else {
        // For other objects, we'll let Axios handle them
        result[key] = value;
      }
      return;
    }
    
    // Handle simple values
    result[key] = value;
  });
  
  return result;
}

/**
 * Utility to check if search criteria is present
 */
export function hasSearchCriteria(params: BaseQueryParams): boolean {
  return !!params.searchText && params.searchText.trim() !== "";
}

/**
 * Utility to check if date filters are present
 */
export function hasDateFilter(params: BaseQueryParams): boolean {
  return !!(
    params.createdAfter ||
    params.createdBefore ||
    params.updatedAfter ||
    params.updatedBefore
  );
}

/**
 * Utility to check if advanced search criteria is present
 */
export function hasAdvancedSearchCriteria(params: BaseQueryParams): boolean {
  return !!(params.searchCriteriaList && params.searchCriteriaList.length > 0);
}

/**
 * Utility to check if field projection is specified
 */
export function hasFieldProjection(params: BaseQueryParams): boolean {
  return !!(
    params.fields &&
    (Array.isArray(params.fields)
      ? params.fields.length > 0
      : params.fields.size > 0)
  );
}

/**
 * Utility to check if grouping is specified
 */
export function hasGrouping(params: BaseQueryParams): boolean {
  return !!(params.groupBy && params.groupBy.length > 0);
}

/**
 * Gets an option value with a fallback default
 */
export function getOption<T>(
  params: BaseQueryParams,
  key: string,
  defaultValue: T
): T {
  if (!params.options || !(key in params.options)) {
    return defaultValue;
  }

  const value = params.options[key];
  return value as unknown as T;
}
