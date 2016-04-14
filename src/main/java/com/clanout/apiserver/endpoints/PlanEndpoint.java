package com.clanout.apiserver.endpoints;

import com.clanout.apiserver.endpoints._core.AbstractEndpoint;
import com.clanout.apiserver.error.ClanoutException;
import com.clanout.apiserver.error.Error;
import com.clanout.apiserver.request.core.SessionRequest;
import com.clanout.application.core.Module;
import com.clanout.application.framework.module.InvalidFieldException;
import com.clanout.application.module.plan.context.PlanContext;
import com.clanout.application.module.plan.domain.exception.DeletePlanPermissionException;
import com.clanout.application.module.plan.domain.exception.FeedNotFoundException;
import com.clanout.application.module.plan.domain.exception.PlanNotFoundException;
import com.clanout.application.module.plan.domain.exception.UpdatePlanPermissionException;
import com.clanout.application.module.plan.domain.use_case.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import java.time.OffsetDateTime;

@Path("plan")
public class PlanEndpoint extends AbstractEndpoint
{
    private PlanContext planContext;

    public PlanEndpoint()
    {
        planContext = (PlanContext) applicationContext.getContext(Module.PLAN);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void getPlan(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                FetchPlan fetchPlan = planContext.fetchPlan();

                FetchPlan.Request request = new FetchPlan.Request();
                request.userId = apiRequest.getSessionUser();
                request.planId = apiRequest.get("plan_id");
                FetchPlan.Response response = fetchPlan.execute(request);

                asyncResponse.resume(buildSuccessJsonResponse(response));
            }
            catch (InvalidFieldException e)
            {
                asyncResponse.resume(new ClanoutException(Error.of(e)));
            }
            catch (PlanNotFoundException e)
            {
                asyncResponse.resume(new ClanoutException(Error.PLAN_NOT_FOUND));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }

    @Path("/feed")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void getFeed(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                OffsetDateTime lastUpdated = null;
                try
                {
                    lastUpdated = OffsetDateTime.parse(apiRequest.get("last_updated"));
                }
                catch (Exception ignored)
                {
                }

                FetchFeed fetchFeed = planContext.fetchFeed();

                FetchFeed.Request request = new FetchFeed.Request();
                request.userId = apiRequest.getSessionUser();
                request.lastUpdated = lastUpdated;
                FetchFeed.Response response = fetchFeed.execute(request);

                if (!response.isUpdated)
                {
                    asyncResponse.resume(buildNotModifiedResponse());
                }
                else
                {
                    asyncResponse.resume(buildSuccessJsonResponse(response.feed));
                }
            }
            catch (InvalidFieldException e)
            {
                asyncResponse.resume(new ClanoutException(Error.of(e)));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }

    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void createPlan(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                OffsetDateTime startTime = null;
                OffsetDateTime endTime = null;
                Double latitude = null;
                Double longitude = null;
                try
                {
                    startTime = OffsetDateTime.parse(apiRequest.get("start_time"));
                    endTime = OffsetDateTime.parse(apiRequest.get("end_time"));
                    latitude = Double.parseDouble(apiRequest.get("latitude"));
                    longitude = Double.parseDouble(apiRequest.get("longitude"));
                }
                catch (Exception ignored)
                {
                }

                CreatePlan createPlan = planContext.createPlan();

                CreatePlan.Request request = new CreatePlan.Request();
                request.userId = apiRequest.getSessionUser();
                request.title = apiRequest.get("title");
                request.type = apiRequest.get("type");
                request.category = apiRequest.get("category");
                request.locationZone = apiRequest.get("location_zone");
                request.description = apiRequest.get("description");
                request.startTime = startTime;
                request.endTime = endTime;
                request.locationName = apiRequest.get("location_name");
                request.latitude = latitude;
                request.longitude = longitude;
                CreatePlan.Response response = createPlan.execute(request);

                asyncResponse.resume(buildSuccessJsonResponse(response));
            }
            catch (InvalidFieldException e)
            {
                asyncResponse.resume(new ClanoutException(Error.of(e)));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }

    @Path("/edit")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void editPlan(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                OffsetDateTime startTime = null;
                OffsetDateTime endTime = null;
                Double latitude = null;
                Double longitude = null;
                try
                {
                    startTime = OffsetDateTime.parse(apiRequest.get("start_time"));
                    endTime = OffsetDateTime.parse(apiRequest.get("end_time"));
                    latitude = Double.parseDouble(apiRequest.get("latitude"));
                    longitude = Double.parseDouble(apiRequest.get("longitude"));
                }
                catch (Exception ignored)
                {
                }

                UpdatePlan updatePlan = planContext.updatePlan();

                UpdatePlan.Request request = new UpdatePlan.Request();
                request.userId = apiRequest.getSessionUser();
                request.planId = apiRequest.get("plan_id");
                request.description = apiRequest.get("description");
                request.startTime = startTime;
                request.endTime = endTime;
                request.locationName = apiRequest.get("location_name");
                request.latitude = latitude;
                request.longitude = longitude;

                updatePlan.execute(request);

                asyncResponse.resume(buildEmptySuccessResponse());
            }
            catch (InvalidFieldException e)
            {
                asyncResponse.resume(new ClanoutException(Error.of(e)));
            }
            catch (PlanNotFoundException e)
            {
                asyncResponse.resume(new ClanoutException(Error.PLAN_NOT_FOUND));
            }
            catch (UpdatePlanPermissionException e)
            {
                asyncResponse.resume(new ClanoutException(Error.PLAN_EDIT_PERMISSION_DENIED));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }

    @Path("/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void deletePlan(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                DeletePlan deletePlan = planContext.deletePlan();

                DeletePlan.Request request = new DeletePlan.Request();
                request.userId = apiRequest.getSessionUser();
                request.planId = apiRequest.get("plan_id");

                deletePlan.execute(request);

                asyncResponse.resume(buildEmptySuccessResponse());
            }
            catch (InvalidFieldException e)
            {
                asyncResponse.resume(new ClanoutException(Error.of(e)));
            }
            catch (PlanNotFoundException e)
            {
                asyncResponse.resume(new ClanoutException(Error.PLAN_NOT_FOUND));
            }
            catch (DeletePlanPermissionException e)
            {
                asyncResponse.resume(new ClanoutException(Error.PLAN_DELETE_PERMISSION_DENIED));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }

    @Path("/rsvp")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateRsvp(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                UpdateRsvp updateRsvp = planContext.updateRsvp();

                UpdateRsvp.Request request = new UpdateRsvp.Request();
                request.userId = apiRequest.getSessionUser();
                request.planId = apiRequest.get("plan_id");
                request.rsvp = apiRequest.get("rsvp");

                updateRsvp.execute(request);

                asyncResponse.resume(buildEmptySuccessResponse());
            }
            catch (InvalidFieldException e)
            {
                asyncResponse.resume(new ClanoutException(Error.of(e)));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }

    @Path("/status")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateStatus(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                boolean isLastMoment = false;
                try
                {
                    isLastMoment = Boolean.parseBoolean(apiRequest.get("is_last_moment"));
                }
                catch (Exception ignored)
                {
                }

                UpdateStatus updateStatus = planContext.updateStatus();

                UpdateStatus.Request request = new UpdateStatus.Request();
                request.userId = apiRequest.getSessionUser();
                request.planId = apiRequest.get("plan_id");
                request.status = apiRequest.get("status");
                request.isLastMoment = isLastMoment;

                updateStatus.execute(request);

                asyncResponse.resume(buildEmptySuccessResponse());
            }
            catch (InvalidFieldException e)
            {
                asyncResponse.resume(new ClanoutException(Error.of(e)));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }

    @Path("/invite")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void invite(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                Invite invite = planContext.invite();

                Invite.Request request = new Invite.Request();
                request.userId = apiRequest.getSessionUser();
                request.planId = apiRequest.get("plan_id");
                request.invitee = apiRequest.getList("invitee");
                request.mobileInvitee = apiRequest.getList("invitee_mobile");

                invite.execute(request);

                asyncResponse.resume(buildEmptySuccessResponse());
            }
            catch (InvalidFieldException e)
            {
                asyncResponse.resume(new ClanoutException(Error.of(e)));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }

    @Path("/chat-update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void chatUpdate(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                ChatUpdate chatUpdate = planContext.chatUpdate();

                ChatUpdate.Request request = new ChatUpdate.Request();
                request.planId = apiRequest.get("plan_id");
                request.message = apiRequest.get("message");

                chatUpdate.execute(request);
            }
            catch (Exception ignored)
            {
            }

            asyncResponse.resume(buildEmptySuccessResponse());

        });
    }

    @Path("/invitation-response")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void invitationResponse(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                InvitationResponse invitationResponse = planContext.invitationResponse();

                InvitationResponse.Request request = new InvitationResponse.Request();
                request.userId = apiRequest.getSessionUser();
                request.planId = apiRequest.get("plan_id");
                request.invitationResponse = apiRequest.get("invitation_response");

                invitationResponse.execute(request);

                asyncResponse.resume(buildEmptySuccessResponse());
            }
            catch (InvalidFieldException e)
            {
                asyncResponse.resume(new ClanoutException(Error.of(e)));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }

    @Path("/create-suggestions")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void getCreateSuggestions(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                FetchCreatePlanSuggestions fetchCreatePlanSuggestions = planContext.fetchCreatePlanSuggestions();
                FetchCreatePlanSuggestions.Response response = fetchCreatePlanSuggestions.execute();

                asyncResponse.resume(buildSuccessJsonResponse(response));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }

    @Path("/pending-invitations")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void getPendingInvitations(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                FetchPendingInvitations fetchPendingInvitations = planContext.fetchPendingInvitations();

                FetchPendingInvitations.Request request = new FetchPendingInvitations.Request();
                request.userId = apiRequest.getSessionUser();
                request.mobileNumber = apiRequest.get("mobile_number");

                FetchPendingInvitations.Response response = fetchPendingInvitations.execute(request);
                asyncResponse.resume(buildSuccessJsonResponse(response));
            }
            catch (InvalidFieldException e)
            {
                asyncResponse.resume(new ClanoutException(Error.of(e)));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }
}
