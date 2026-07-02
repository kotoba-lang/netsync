(ns netsync-test
  (:require [clojure.test :refer [deftest is]]
            [kotoba.netsync :as net]))

(deftest snapshot-and-interp
  (let [snap (net/snapshot net/default-schema {:x 1 :y 2 :z 3 :rx 0 :ry 0 :hp 9 :tag "bot" :secret 42})]
    (is (= #{:x :y :z :rx :ry :hp} (set (keys snap))))
    (is (not (contains? snap :secret))))
  (let [r (net/interp net/default-schema {:x 0 :hp 100} {:x 10 :hp 50} 0.5)]
    (is (= 5.0 (double (:x r))))
    (is (= 50 (:hp r)))))

(deftest prediction
  (let [step (fn [e input] (update e :x + (:dx input)))
        pending [{:dx 1} {:dx 2}]
        local {:x 3}
        auth {:x 0}]
    (is (= local (net/pred-reconcile net/default-schema local auth pending step)))))
