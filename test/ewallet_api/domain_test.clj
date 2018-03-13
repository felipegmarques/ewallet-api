(ns ewallet-api.domain-test
    (:import java.util.Date)
  (:require [clojure.test :refer :all]
            [ewallet-api.domain :refer :all]))
(def entries
    [
        {:value -100.0 :date (Date. 2015 05 12)},
        {:value -50.0 :date (Date. 2015 05 15)},
        {:value 30.0 :date (Date. 2015 05 13)},
        {:value 20.0 :date (Date. 2015 05 23)},
        {:value 300.0 :date (Date. 2015 04 12)},
        {:value -100.0 :date (Date. 2015 04 22)},
        {:value 100 :date (Date. 2015 06 01)}
    ])

(def entry {:value -100.0 :date (Date. 2015 05 21) :description "Sorvete"})

(deftest summarize-period-test
    (testing "Summarize month"
    (is (= 
        {:current-amount 100.0 :previous-amount 200.0 :debits -150.0 :credits 50.0}
        (summarize-period (Date. 2015 05 1) (Date. 2015 05 31) entries)))))

(deftest get-entries-test
    (testing "Get entries by date"
    (is (=
        [{:value -100.0 :date (Date. 2015 05 12)},
        {:value -50.0 :date (Date. 2015 05 15)},
        {:value 30.0 :date (Date. 2015 05 13)},
        {:value 20.0 :date (Date. 2015 05 23)}]
        (get-entries (Date. 2015 05 1) (Date. 2015 05 31) entries)))))

(deftest add-entry-test
    (testing "Add entry"
        (is (some #{entry} (add-entry entries entry)))))