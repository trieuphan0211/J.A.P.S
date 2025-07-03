package vn.stephen.authservice.exception;

import vn.stephen.authservice.constants.ErrorCode;

public class EntityNotFoundException extends GlobalException{
    public EntityNotFoundException() {
        super(ErrorCode.NOT_FOUND,"Requested entity not present in the DB." );
    }
    public EntityNotFoundException (String message) {
        super(ErrorCode.NOT_FOUND,message);
    }
}
