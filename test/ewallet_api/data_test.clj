(ns ewallet-api.data-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as time]
            [ewallet-api.data.data :refer :all]))
(def raw-entries
"value;date;description
-100.0;2015/05/12;Sorvete
-50.0;2015/05/15;Carrefour
30.0;2015/05/13;Viagem
20.0;2015/05/23;Cerveja
300.0;2015/04/12;Piadina
-100.0;2015/04/22;Kebab
100;2015/06/01;Carrefour")

(def entries
    [
        {:value -100.0 :date (time/date-time 2015 05 12) :description "Sorvete"},
        {:value -50.0 :date (time/date-time 2015 05 15) :description "Carrefour"},
        {:value 30.0 :date (time/date-time 2015 05 13) :description "Viagem"},
        {:value 20.0 :date (time/date-time 2015 05 23) :description "Cerveja"},
        {:value 300.0 :date (time/date-time 2015 04 12) :description "Piadina"},
        {:value -100.0 :date (time/date-time 2015 04 22) :description "Kebab"},
        {:value 100 :date (time/date-time 2015 06 01) :description "Carrefour"}
    ])

(deftest parse-raw-entries-test
    (testing "Parse Raw Entries"
    (is (= 
        entries
        (parse-raw-entries raw-entries)))))

(deftest entries-to-csv-test
    (testing "Produces CSV string from Raw entriesl"
    (is (=
            raw-entries
            (entries-to-csv entries)))))
