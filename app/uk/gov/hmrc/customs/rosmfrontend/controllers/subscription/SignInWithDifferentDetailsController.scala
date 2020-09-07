/*
 * Copyright 2020 HM Revenue & Customs
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

package uk.gov.hmrc.customs.rosmfrontend.controllers.subscription

import javax.inject.{Inject, Singleton}
import play.api.Application
import play.api.mvc._
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.customs.rosmfrontend.controllers.CdsController
import uk.gov.hmrc.customs.rosmfrontend.domain._
import uk.gov.hmrc.customs.rosmfrontend.models.Journey
import uk.gov.hmrc.customs.rosmfrontend.services.cache.SessionCache
import uk.gov.hmrc.customs.rosmfrontend.views.html.subscription.sign_in_with_different_details

import scala.concurrent.ExecutionContext

@Singleton
class SignInWithDifferentDetailsController @Inject()(
  override val currentApp: Application,
  override val authConnector: AuthConnector,
  cdsFrontendDataCache: SessionCache,
  signInWithDifferentDetailsView: sign_in_with_different_details,
  mcc: MessagesControllerComponents
)(implicit ec: ExecutionContext)
    extends CdsController(mcc) {

  def form(journey: Journey.Value): Action[AnyContent] = ggAuthorisedUserWithEnrolmentsAction {
    implicit request => _: LoggedInUserWithEnrolments =>
      val name = journey match {
        case Journey.GetYourEORI => cdsFrontendDataCache.registrationDetails.map(_.name)
        case Journey.Migrate     => cdsFrontendDataCache.subscriptionDetails.map(_.name)
        case _                   => throw new IllegalArgumentException("No a valid journey")
      }

      name map { n =>
        val optionalName = Option(n) filter (_.nonEmpty)
        Ok(signInWithDifferentDetailsView(optionalName))
      }
  }
}
