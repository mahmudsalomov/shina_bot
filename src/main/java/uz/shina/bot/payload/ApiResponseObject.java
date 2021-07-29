package uz.shina.bot.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ApiResponseObject extends ApiResponse{
    private Object object;
    private long totalElements;
    private long totalPages;

    public ApiResponseObject(String message, boolean success, Object object) {
        super(message, success);
        this.object = object;
    }

    public ApiResponseObject(Object object, long totalElements, long totalPages) {
        this.object = object;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public ApiResponseObject(String message, boolean success, Object object, long totalElements, long totalPages) {
        super(message, success);
        this.object = object;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}
