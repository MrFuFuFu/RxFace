package mrfu.rxface.models;

import mrfu.rxface.loader.WebServiceException;
import rx.Observable;

/**
 * Created by MrFu on 16/1/10.
 */
public class BaseResponse {
    public Observable filterWebServiceErrors() {
        if (true){//judge result status is ok~~
            return Observable.just(this);
        }else {
            return Observable.error(new WebServiceException("Service return Error message"));
        }
        //demo code just like blow, the common is a class object, you can deal the error code in here.
//        public class BaseResponse {
//            public Common common;
//
//            public Observable filterWebServiceErrors() {
//                if (common == null){
//                    return Observable.error(new WebServiceException("啊喔,服务器除了点小问题"));
//                }else {
//                    int code = Integer.parseInt(common.status);
//                    switch (code){
//                        case Constants.RESULT_OK://正常
//                            return Observable.just(this);
//                        case Constants.RESULT_NORMAL_UPDATE://普通升级
//                        case Constants.RESULT_FORCE_UPDATE://墙纸升级
//                            if (AppApplication.getInstance().updateModel == null) {
//                                AppApplication.getInstance().updateModel = common.update;
//                                AppApplication.getInstance().updateModel.code = code;
//                            }
//                            return Observable.just(this);
//                        default://出错
//                            return Observable.error(new WebServiceException(BaseResponse.this.common.memo));
//                    }
//                }
//            }
//        }
    }
}
