(ns draw.core
  (:import (java.awt Color Graphics2D Image RenderingHints)
           (java.awt.image BufferedImage)))


(defn rgb
  ([^Integer r ^Integer g ^Integer b] (rgb r g b 255))
  ([^Integer r ^Integer g ^Integer b ^Integer a] (Color. r g b a)))

;;; instructions ;;;

(defn pixel
  ([x y colour] (pixel x y colour 0))
  ([x y colour z]
   {:op     :pixel
    :x      x
    :y      y
    :z      z
    :colour colour}))

;;; render ;;;

(defmulti render (fn [_target instruction] (:op instruction)))

(defmethod render :pixel [^Graphics2D graphics2d {:keys [x y colour]}]
  (.setColor graphics2d colour)
  (.drawLine graphics2d x y x y))

;;; draw ;;;

(defn new-image [width height]
  (BufferedImage. width height BufferedImage/TYPE_INT_ARGB))

(defn draw [^Image image & instructions]
  (let [graphics ^Graphics2D (.getGraphics image)]
    (.setRenderingHint graphics RenderingHints/KEY_ANTIALIASING RenderingHints/VALUE_ANTIALIAS_ON)
    (->> instructions
         (sort-by :z)
         (map (partial render graphics)))))
