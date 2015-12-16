package mrfu.rxface.models;

import java.util.List;

/**
 * Created by MrFu on 15/12/16.
 */
public class FacePlusPlusEntity {

    /**
     * face : [{"attribute":{"age":{"range":6,"value":18},"gender":{"confidence":99.9996,"value":"Male"},"race":{"confidence":99.8977,"value":"White"},"smiling":{"value":81.2229}},"face_id":"5bf244c54d5731974e25ee024b950cd3","position":{"center":{"x":47.833333,"y":49.036403},"eye_left":{"x":42.140333,"y":42.28394},"eye_right":{"x":53.658,"y":42.389936},"height":31.263383,"mouth_left":{"x":42.352167,"y":56.311991},"mouth_right":{"x":53.747833,"y":56.89379},"nose":{"x":47.392667,"y":51.794004},"width":24.333333},"tag":""},{"attribute":{"age":{"range":5,"value":24},"gender":{"confidence":99.5758,"value":"Male"},"race":{"confidence":99.94800000000001,"value":"White"},"smiling":{"value":93.0865}},"face_id":"092cb7660d3f8d115b8af331465624a1","position":{"center":{"x":83.833333,"y":52.462527},"eye_left":{"x":77.052667,"y":47.005353},"eye_right":{"x":87.198,"y":44.253961},"height":28.265525,"mouth_left":{"x":77.304167,"y":60.063169},"mouth_right":{"x":86.635167,"y":60.254176},"nose":{"x":86.9965,"y":54.840685},"width":22},"tag":""},{"attribute":{"age":{"range":11,"value":38},"gender":{"confidence":74.2956,"value":"Female"},"race":{"confidence":96.8155,"value":"White"},"smiling":{"value":86.556}},"face_id":"3c9d898f732c84d07d760f184bfec814","position":{"center":{"x":13,"y":52.248394},"eye_left":{"x":8.483217,"y":44.584797},"eye_right":{"x":18.669667,"y":46.517987},"height":27.837259,"mouth_left":{"x":8.44005,"y":60.162955},"mouth_right":{"x":17.914333,"y":60.197645},"nose":{"x":8.59085,"y":53.623769},"width":21.666667},"tag":""}]
     * img_height : 490
     * img_id : f3f8c2826537ce51ca1995143e8b9289
     * img_width : 629
     * session_id : a7f871065a064bdfabe06de48189dcac
     * url : http://imglife.gmw.cn/attachement/jpg/site2/20111014/002564a5d7d21002188831.jpg
     */

    public int img_height;
    public String img_id;
    public int img_width;
    public String session_id;
    public String url;
    /**
     * attribute : {"age":{"range":6,"value":18},"gender":{"confidence":99.9996,"value":"Male"},"race":{"confidence":99.8977,"value":"White"},"smiling":{"value":81.2229}}
     * face_id : 5bf244c54d5731974e25ee024b950cd3
     * position : {"center":{"x":47.833333,"y":49.036403},"eye_left":{"x":42.140333,"y":42.28394},"eye_right":{"x":53.658,"y":42.389936},"height":31.263383,"mouth_left":{"x":42.352167,"y":56.311991},"mouth_right":{"x":53.747833,"y":56.89379},"nose":{"x":47.392667,"y":51.794004},"width":24.333333}
     * tag :
     */

    public List<FaceEntity> face;

    public static class FaceEntity {
        /**
         * age : {"range":6,"value":18}
         * gender : {"confidence":99.9996,"value":"Male"}
         * race : {"confidence":99.8977,"value":"White"}
         * smiling : {"value":81.2229}
         */

        public AttributeEntity attribute;
        public String face_id;
        /**
         * center : {"x":47.833333,"y":49.036403}
         * eye_left : {"x":42.140333,"y":42.28394}
         * eye_right : {"x":53.658,"y":42.389936}
         * height : 31.263383
         * mouth_left : {"x":42.352167,"y":56.311991}
         * mouth_right : {"x":53.747833,"y":56.89379}
         * nose : {"x":47.392667,"y":51.794004}
         * width : 24.333333
         */

        public PositionEntity position;
        public String tag;

        public static class AttributeEntity {
            /**
             * range : 6
             * value : 18
             */

            public AgeEntity age;
            /**
             * confidence : 99.9996
             * value : Male
             */

            public GenderEntity gender;
            /**
             * confidence : 99.8977
             * value : White
             */

            public RaceEntity race;
            /**
             * value : 81.2229
             */

            public SmilingEntity smiling;

            public static class AgeEntity {
                public int range;
                public int value;
            }

            public static class GenderEntity {
                public double confidence;
                public String value;
            }

            public static class RaceEntity {
                public double confidence;
                public String value;
            }

            public static class SmilingEntity {
                public double value;
            }
        }

        public static class PositionEntity {
            /**
             * x : 47.833333
             * y : 49.036403
             */

            public CenterEntity center;
            /**
             * x : 42.140333
             * y : 42.28394
             */

            public EyeLeftEntity eye_left;
            /**
             * x : 53.658
             * y : 42.389936
             */

            public EyeRightEntity eye_right;
            public double height;
            /**
             * x : 42.352167
             * y : 56.311991
             */

            public MouthLeftEntity mouth_left;
            /**
             * x : 53.747833
             * y : 56.89379
             */

            public MouthRightEntity mouth_right;
            /**
             * x : 47.392667
             * y : 51.794004
             */

            public NoseEntity nose;
            public double width;

            public static class CenterEntity {
                public double x;
                public double y;
            }

            public static class EyeLeftEntity {
                public double x;
                public double y;
            }

            public static class EyeRightEntity {
                public double x;
                public double y;
            }

            public static class MouthLeftEntity {
                public double x;
                public double y;
            }

            public static class MouthRightEntity {
                public double x;
                public double y;
            }

            public static class NoseEntity {
                public double x;
                public double y;
            }
        }
    }
}
