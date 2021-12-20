package org.dx.clod.lark.sdk.request;

import lombok.Data;
import org.dx.clod.lark.sdk.model.Dimension;

/**
 * @author lizifeng
 * @date 2021/12/22 17:08
 */
@Data
public class InsertDimensionRangeRequest {

    private Dimension dimension;

    private String inheritStyle;
}
