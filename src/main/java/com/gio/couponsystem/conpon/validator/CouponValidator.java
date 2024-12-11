package com.gio.couponsystem.conpon.validator;

import com.gio.couponsystem.conpon.domain.Coupon;
import com.gio.couponsystem.exception.CustomException;
import com.gio.couponsystem.exception.ExceptionCode;
import io.micrometer.common.util.StringUtils;

@Validator
public class CouponValidator {
    public static final int COUPON_TITLE_MAX_LENGTH = 100;

    public void validate(Coupon coupon) {
        if (coupon.getQuantity() < 0) {
            throw new CustomException(ExceptionCode.COUPON_INVALID_QUANTITY);
        }

        if (isCouponEndDateInvalid(coupon) ) {
            throw new CustomException(ExceptionCode.COUPON_INVALID_DATE);
        }

        if (StringUtils.isBlank(coupon.getTitle())) {
            throw new CustomException(ExceptionCode.COUPON_EMPTY_TITLE);
        }

        if (isCouponTitleLengthInValid(coupon)) {
            throw new CustomException(ExceptionCode.COUPON_INVALID_TITLE);
        }
    }

    private  boolean isCouponTitleLengthInValid(Coupon coupon) {
        return coupon.getTitle().length() > COUPON_TITLE_MAX_LENGTH;
    }

    private boolean isCouponEndDateInvalid(Coupon coupon) {
        return coupon.getStartDate().isEqual(coupon.getEndDate()) || coupon.getStartDate().isAfter(coupon.getEndDate());
    }
}
