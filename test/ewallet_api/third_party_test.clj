(ns ewallet-api.third-party-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as time]
            [clj-time.format :as f]))

(def date-formater (f/formatter "yyyy/MM/dd"))

(deftest get-entries-by-period-test
    (testing "Get Entries for Date"
    (is (thrown? IllegalArgumentException (f/parse date-formater "2015/1105")))))
