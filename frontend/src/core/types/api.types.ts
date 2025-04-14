export type ApiResponse<T> =  {
  data: T;
  status: string;
  message: string;
  error: string;
}

export type ApiListResponse<T> =  {
  data: {
    content: T[];
    pageable: {
      pageNumber: number;
      pageSize: number;
      sort: {
        empty: boolean;
        sorted: boolean;
        unsorted: boolean;
      };
      offset: number;
      paged: boolean;
      unpaged: boolean;
    };
    hasNext: boolean;
    hasPrevious: boolean;
    last: boolean;
    totalElements: number;
    totalPages: number;
    first: boolean;
    size: number;
    number: number;
    numberOfElements: number;
    empty: boolean;
  }
  message: string;
  status: string;
  error: string;
}