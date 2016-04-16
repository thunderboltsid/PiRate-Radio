class RequestController < ApplicationController
  skip_before_action :verify_authenticity_token
  before_action :set_request, only: [:show, :destroy]
  before_action :set_client,   only: [:send_sms, :create]

  def index
    @requests = Request.all.reverse
  end

  def show
  end

  def new
    @requests = Request.new
  end

  def send_sms
    @client.messages.create(
      from: ENV["TWILIO_PHONE_NUMBER"],
      to:   ENV["MY_PHONE_NUMBER"],
      body: 'Hey there!'
    )
  end

  def get_shorten_link
    Google::UrlShortener.shorten!(new_request_url(params[:request_id]))
  end

  def feed
    @requests = Request.all.reverse
    respond_to do |format|
      format.rss { render :layout => false }
    end
  end

  def create
    @request = Request.new(twilio_request_params)
    if @request.save
      puts "New request (#{twilio_request_params[:phone]}): #{twilio_request_params[:message]}"
    else
      puts "There was an error with the reqest (#{twilio_request_params[:phone]}): #{twilio_request_params[:message]}"
    end
  end

  def destroy
    @request.destroy
    redirect_to requests_url, notice: 'Request was successfully destroyed.'
  end

  private
    def set_client
      @client = Twilio::REST::Client.new
    end

    def set_request
      @request = Request.find(params[:id])
    end

    def request_params
      params.require(:request).permit(:message, :phone)
    end

    def twilio_request_params
      {
        phone: params["From"],
        message: params["Body"]
      }
    end
end
