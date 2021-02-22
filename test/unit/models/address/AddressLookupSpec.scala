/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package unit.models.address

import base.UnitSpec
import play.api.libs.json.Json
import uk.gov.hmrc.eoricommoncomponent.frontend.models.address.AddressLookup

class AddressLookupSpec extends UnitSpec {

  "Address Lookup model" should {

    "correctly read only required information from json" in {

      val addressJsonResponse = Json.parse("""
          |{
          |  "id": "id",
          |  "uprn": 1234,
          |  "address": {
          |    "lines": [
          |      "Address Line 1",
          |      "Address Line 2",
          |      "Address Line 3"
          |    ],
          |    "town": "Town",
          |    "county": "County",
          |    "postcode": "AA11 1AA",
          |    "subdivision": {
          |      "code": "GB-ENG",
          |      "name": "England"
          |    },
          |    "country": {
          |      "code": "UK",
          |      "name": "United Kingdom"
          |    }
          |  },
          |  "language": "en",
          |  "localCustodian": {
          |    "code": 123,
          |    "name": "Name"
          |  }
          |}""".stripMargin)

      val result = AddressLookup.addressReads.reads(addressJsonResponse)

      val expectedModel = AddressLookup("Address Line 1 Address Line 2", "Town", "AA11 1AA", "GB")

      result.get shouldBe expectedModel
    }
  }
}
